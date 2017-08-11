import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import 'hammerjs';
import { AppComponent } from './app.component';

import {AppRoutingModule} from "./routing";
import {AppMaterialModule} from "./app.material.module";
import { MonitorComponent } from './monitor/monitor.component';
import { EmployeeComponent } from './employee/employee.component';
import { DeviceComponent } from './device/device.component';
import { JobComponent } from './job/job.component';
import {HttpModule} from "@angular/http";
import { DeviceEditComponent } from './device-edit/device-edit.component';
import { YesNoDialogComponent } from './yes-no-dialog/yes-no-dialog.component';
import { FormsModule } from '@angular/forms';
import { EmployeeEditComponent } from './employee-edit/employee-edit.component';





@NgModule({
  declarations: [
    AppComponent,
    MonitorComponent,
    EmployeeComponent,
    DeviceComponent,
    JobComponent,
    DeviceEditComponent,
    YesNoDialogComponent,
    EmployeeEditComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    AppMaterialModule,
    HttpModule,
    FormsModule
  ],
  entryComponents: [YesNoDialogComponent],
  providers: [HttpModule],
  bootstrap: [AppComponent]
})

export class AppModule { }
