import {Employee} from "./Employee";
import {Device} from "./Device";
/**
 * Created by vitaly on 09.08.17.
 */
export class Job{

  id:number;
  startDate:Date;
  stopDate:Date;
  lastExchange:Date;
  employee:Employee;
  device:Device;
  durability:number;
  active:number;
  difficult:number;
  salaryRate:number;

  //вспомогательные поля
  hour:string;
  min:string;
  sec:string;


}
