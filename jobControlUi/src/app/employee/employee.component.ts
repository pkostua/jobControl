import {Component, Inject, OnInit} from '@angular/core';
import {DataSource} from '@angular/cdk';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/merge';
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/map';
import {Router} from "@angular/router";
import {YesNoDialogComponent} from "../yes-no-dialog/yes-no-dialog.component";
import {MdDialog, MdSnackBar} from "@angular/material";
import {Employee} from "../entity/Employee";

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {

  constructor(@Inject(Http) public http: Http,
              @Inject(Router) public router: Router,
              @Inject(MdDialog) public dialog: MdDialog,
              @Inject(MdSnackBar) private snackBar: MdSnackBar,) {}

  displayedColumns = ['Id', 'firstName', 'lastName', 'role', 'salaryRate', 'edit'];
  employeeDatabase = new EmployeeDatabase(this.http);
  dataSource: EmployeeDataSource | null;

  ngOnInit() {
    this.dataSource = new EmployeeDataSource(this.employeeDatabase);
  }
  gotoEdit(id:number){

    this.router.navigate(['/employeeEdit', id]);
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
    return this.http.delete("api/employee/delete?id="+id)
      .map((res: Response) => {
        return res.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}

export class EmployeeDatabase {
  /** Stream that emits whenever the data has been modified. */
  dataChange: BehaviorSubject<Employee[]> = new BehaviorSubject<Employee[]>([]);
  get data(): Employee[] { return this.dataChange.value; }

  constructor(@Inject(Http) public http: Http) {
    this.update();
  }

  getAll():Observable<Employee[]>{
    return this.http.get("api/employee/getAll")
      .map((res: Response) => {return res.json();})
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  update():Observable<Employee[]>{
    this.getAll()
      .subscribe(employee=>{
        this.dataChange.next(employee);
      });
    return this.dataChange;

  }
}

export class EmployeeDataSource extends DataSource<any> {
  constructor(private _employeeDatabase: EmployeeDatabase) {
    super();
  }

  connect(): Observable<Employee[]> {
    return this._employeeDatabase.dataChange;
  }
  update():Observable<Employee[]>{
    return this._employeeDatabase.update();
  }

  disconnect() {}
}

