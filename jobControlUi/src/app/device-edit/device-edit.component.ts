import {Component, Inject, OnInit} from '@angular/core';
import {Device} from "../entity/Device";
import {Http, Response,Headers} from "@angular/http";
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {ActivatedRoute} from "@angular/router";
import {MdSnackBar} from "@angular/material";
import { Location }  from '@angular/common';

@Component({
  selector: 'app-device-edit',
  templateUrl: './device-edit.component.html',
  styleUrls: ['./device-edit.component.css']
})
export class DeviceEditComponent implements OnInit {

  constructor(@Inject(Http) public http: Http,
              @Inject(ActivatedRoute) private route: ActivatedRoute,
              @Inject(Location)private location: Location,
              @Inject(MdSnackBar) private snackBar: MdSnackBar
  ) { }

  device:Device = new Device();
  id:number;

  ngOnInit() {

    this.route.params.subscribe(params => {
      // Defaults to 0 if no query param provided.
      this.id = +params['id'] || 0;
      if (this.id != 0) {
        this.getById(this.id)
          .subscribe(device => this.device = device);
      }
    });
  }

  saveDevice(){
    if(this.device.name == "") {
      this.snackBar.open("Введите имя", "", {duration: 2000,});
      return;
    }
    if(this.device.difficult == 0) {
      this.snackBar.open("Сложность не может быть 0", "", {duration: 2000,});
      return;
    }
    this.save(this.device)
      .subscribe(device=>{
        this.device =device;
        this.snackBar.open("Сохранено", "", {duration: 2000,});
      },
      err=>{
        this.snackBar.open("Ошибка сохранения "+err, "", {duration: 2000,});
      });

  }
  goBack():void{
    this.location.back();

  }

  getById(id:number):Observable<Device> {
    return this.http.get("api/device?id="+id)
      .map((res: Response) => {
        return res.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  save(device:Device):Observable<Device>{
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this.http.post("/api/device",device,{headers})
      .map((res: Response) =>res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));

  }
}
