import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { PopupComponent } from './popup/popup.component';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path:"login",
    component: LoginComponent
  },
  {
    path:"code",
    component: PopupComponent
  }
]

@NgModule({
  declarations: [LoginComponent, PopupComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class AuthModule { }
