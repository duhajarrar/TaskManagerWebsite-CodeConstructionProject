import { Role, Gender } from "../constants/enum/user.enum"

export interface User{
    id:string,
    name:string,
    password: string,
    email: string,
    gender: Gender,
    role: Role,
    isActive: boolean
  }