import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import 'hammerjs';
import { AppComponent } from './app.component';
import {WebsocketService} from "./websocket.service";
import {AppRoutingModule} from "./routing";
import {AppMaterialModule} from "./app.material.module";
import { MonitorComponent } from './monitor/monitor.component';
import { EmployeeComponent } from './employee/employee.component';
import { DeviceComponent } from './device/device.component';
import { JobComponent } from './job/job.component';
import {HttpModule} from "@angular/http";





@NgModule({
  declarations: [
    AppComponent,
    MonitorComponent,
    EmployeeComponent,
    DeviceComponent,
    JobComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    AppMaterialModule,
    HttpModule,
  ],
  providers: [WebsocketService,HttpModule
    ],
  bootstrap: [AppComponent]
})

export class AppModule { }
