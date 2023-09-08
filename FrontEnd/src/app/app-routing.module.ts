import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { RegisterAppComponent } from './register-app/register-app.component';
import { ViewAppComponent } from './view-app/view-app.component';

const routes: Routes = [
  { path: "*", component: AppComponent },
  { path: "register", component: RegisterAppComponent },
  { path:"view", component: ViewAppComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
