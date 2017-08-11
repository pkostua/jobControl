/**
 * Created by vitaly on 09.08.17.
 */
import { Routes, RouterModule } from '@angular/router';
import { NgModule }             from '@angular/core';
import {MonitorComponent} from "./monitor/monitor.component";
import {DeviceComponent} from "./device/device.component";
import {EmployeeComponent} from "./employee/employee.component";
import {JobComponent} from "./job/job.component";
import {DeviceEditComponent} from "./device-edit/device-edit.component";
import {EmployeeEditComponent} from "./employee-edit/employee-edit.component";


export const routes: Routes = [
   { path: '', component: MonitorComponent},
   { path: 'monitor', component: MonitorComponent},
   { path: 'device', component: DeviceComponent},
   { path: 'employee', component: EmployeeComponent},
   { path: 'job', component: JobComponent},

   { path: 'deviceEdit/:id', component: DeviceEditComponent},
   { path: 'employeeEdit/:id', component: EmployeeEditComponent},



];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
