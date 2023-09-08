import { Component, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'; // Asegúrate de importar HttpClientModule
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { RegisterAppComponent } from './register-app/register-app.component';
import { ViewAppComponent } from './view-app/view-app.component';
import { CouplesComponent } from './register-app/couples/couples.component';
import { CreditRequestComponent } from './register-app/credit-request/credit-request.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterAppComponent,
    ViewAppComponent,
    CouplesComponent,
    CreditRequestComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
