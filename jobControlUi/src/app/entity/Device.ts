/**
 * Created by vitaly on 09.08.17.
 */
import {Job} from "./Job";

export class Device{

  id:number;
  name:string;
  description:string;
  mac:string;
  ip:string;
  currentJob:Job;
}
