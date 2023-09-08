import { Component, OnInit, Injectable } from '@angular/core';
import { ComponentPortal } from '@angular/cdk/portal';
import { CouplesComponent } from './couples/couples.component';
import { CreditRequestComponent } from './credit-request/credit-request.component';

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-register-app',
  templateUrl: './register-app.component.html',
  styleUrls: ['./register-app.component.css']
})

export class RegisterAppComponent implements OnInit{

  constructor() {
  }

  ngOnInit() {
  }

  renderAnotherComponent() {
    const componentPortal = new ComponentPortal(CouplesComponent);
    componentPortal.activate(document.querySelector('#another-component'));
  }

  renderAnotherComponent2() {
    const componentPortal = new ComponentPortal(CreditRequestComponent);
    componentPortal.activate(document.querySelector('#another-component'));
  }
  

}
