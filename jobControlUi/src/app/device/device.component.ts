import {Component, Inject, OnInit} from '@angular/core';
import {DataSource} from '@angular/cdk';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/merge';
import {Device} from "../entity/Device";
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/map';
import {Router} from "@angular/router";
import {YesNoDialogComponent} from "../yes-no-dialog/yes-no-dialog.component";
import {MdDialog, MdSnackBar} from "@angular/material";


@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent implements OnInit {

  constructor(@Inject(Http) public http: Http,
              @Inject(Router) public router: Router,
              @Inject(MdDialog) public dialog: MdDialog,
              @Inject(MdSnackBar) private snackBar: MdSnackBar,) {}

  displayedColumns = ['Id', 'name', 'difficult', 'workTime', 'edit'];
  deviceDatabase = new DeviceDatabase(this.http);
  dataSource: DeviceDataSource | null;

  ngOnInit() {
    this.dataSource = new DeviceDataSource(this.deviceDatabase);
  }
  gotoEdit(id:number){

    this.router.navigate(['/deviceEdit', id]);
  }

  deleteDevice(id:number){
    let dialogRef = this.dialog.open(YesNoDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result=='yes') {
        this.deleteById(id)
          .subscribe(res => {
              this.snackBar.open("Удален", "", {
                duration: 2000,
              });
              this.dataSource.update();
            },
            err => {
              this.snackBar.open("Ошибка удаления " + err, "", {
                duration: 2000,
              });
            });
      }
    });
  }

  deleteById(id:number):Observable<String> {
    return this.http.delete("api/device/delete?id="+id)
      .map((res: Response) => {
        return res.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}

export class DeviceDatabase {
  /** Stream that emits whenever the data has been modified. */
  dataChange: BehaviorSubject<Device[]> = new BehaviorSubject<Device[]>([]);
  get data(): Device[] { return this.dataChange.value; }

  constructor(@Inject(Http) public http: Http) {
    this.update();
  }

  getAll():Observable<Device[]>{
    return this.http.get("api/device/getAll")
      .map((res: Response) => {return res.json();})
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  update():Observable<Device[]>{
     this.getAll()
      .subscribe(devices=>{
        this.dataChange.next(devices);
      });
    return this.dataChange;

  }
}

export class DeviceDataSource extends DataSource<any> {
  constructor(private _deviceDatabase: DeviceDatabase) {
    super();
  }

  connect(): Observable<Device[]> {
    return this._deviceDatabase.dataChange;
  }
  update():Observable<Device[]>{
    return this._deviceDatabase.update();
  }

  disconnect() {}
}

