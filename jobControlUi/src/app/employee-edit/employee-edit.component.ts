import {Component, Inject, OnInit} from '@angular/core';
import {Http, Response,Headers} from "@angular/http";
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {ActivatedRoute} from "@angular/router";
import {MdSnackBar} from "@angular/material";
import { Location }  from '@angular/common';
import {Employee} from "../entity/Employee";

@Component({
  selector: 'app-employee-edit',
  templateUrl: './employee-edit.component.html',
  styleUrls: ['./employee-edit.component.css']
})
export class EmployeeEditComponent implements OnInit {

  constructor(@Inject(Http) public http: Http,
              @Inject(ActivatedRoute) private route: ActivatedRoute,
              @Inject(Location)private location: Location,
              @Inject(MdSnackBar) private snackBar: MdSnackBar
  ) { }

  employee:Employee = new Employee();
  id:number;

  ngOnInit() {

    this.route.params.subscribe(params => {
      // Defaults to 0 if no query param provided.
      this.id = +params['id'] || 0;
      if (this.id != 0) {
        this.getById(this.id)
          .subscribe(employee => this.employee = employee);
      }
    });
  }

  saveEmployee(){

    this.save(this.employee)
      .subscribe(employee=>{
          this.employee = employee;
          this.snackBar.open("Сохранено", "", {duration: 2000,});
        },
        err=>{
          this.snackBar.open("Ошибка сохранения "+err, "", {duration: 2000,});
        });

  }
  goBack():void{
    this.location.back();

  }

  getById(id:number):Observable<Employee> {
    return this.http.get("api/employee?id="+id)
      .map((res: Response) => {
        return res.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  save(employee:Employee):Observable<Employee>{
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this.http.post("/api/employee",employee,{headers})
      .map((res: Response) =>res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));

  }
}
