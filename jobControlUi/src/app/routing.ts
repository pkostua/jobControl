/**
 * Created by vitaly on 09.08.17.
 */
import { Routes, RouterModule } from '@angular/router';
import { NgModule }             from '@angular/core';
import {MonitorComponent} from "./monitor/monitor.component";
import {DeviceComponent} from "./device/device.component";
import {EmployeeComponent} from "./employee/employee.component";
import {JobComponent} from "./job/job.component";


export const routes: Routes = [
   { path: '', component: MonitorComponent},
   { path: 'monitor', component: MonitorComponent},
   { path: 'device', component: DeviceComponent},
  { path: 'employee', component: EmployeeComponent},
  { path: 'job', component: JobComponent},



];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
