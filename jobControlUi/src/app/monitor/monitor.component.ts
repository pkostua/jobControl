import {Component, Inject, OnInit} from '@angular/core';

import {Device} from "../entity/Device";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import * as moment from 'moment';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import {Http, Response} from "@angular/http";
import {Subscription} from "rxjs/Subscription";


@Component({
  selector: 'app-monitor',
  templateUrl: './monitor.component.html',
  styleUrls: ['./monitor.component.css']
})

export class MonitorComponent implements OnInit {

  public devices: Device[];
  stompClient: any;
  private timer;
  private sub: Subscription;

  constructor(@Inject(Http) public http: Http) {

  }

  ngOnInit() {
    moment.locale('ru');
    this.getMonitorStartPosition()
      .subscribe(devices=>{
        this.devices=devices;
        this.timer = Observable.timer(1000,1000);
        this.sub = this.timer.subscribe(t => {
            this.devices.forEach(d=>{
              if(d.currentJob !== null) {
                let duration = new Date().getTime() - new Date(d.currentJob.startDate).getTime();
                d.currentJob.hour = moment.duration(duration).hours()+"";
                let min = moment.duration(duration).minutes();
                if (min < 10) d.currentJob.min = "0"+ min;
                else d.currentJob.min = ""+ min;

                let sec = moment.duration(duration).seconds();
                if (sec < 10) d.currentJob.sec = "0"+ sec;
                else d.currentJob.sec = ""+ sec;

              }
          })
        });
      });


    var that = this;
    var socket = new SockJS('/monitor');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      that.stompClient.subscribe('/topic/monitorUpdate' , function (res) {
        console.log("message");
        that.devices = JSON.parse(res.body);
      });
    }, function (err) {
      console.log('err', err);
    });

  }

  getMonitorStartPosition():Observable<Device[]>{

    return this.http.get("api/monitor")
      .map((res: Response) => {return res.json();})
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

}
