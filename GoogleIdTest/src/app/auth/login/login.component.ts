import { Component, OnInit, HostListener } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private authService : AuthService,
              private router : Router) { }

  ngOnInit() {
    this.authService.login();
  }


  @HostListener('window:message', ['$event']) onMessage(event) {
    if (event.origin === 'http://localhost:4200' && event.data.type === 'oauth') {
      this.authService.authorize(event.data.value)
        .then(() => {
          const redirectUrl = sessionStorage.getItem('redirectUrl');
          sessionStorage.removeItem('redirectUrl');
          this.router.navigateByUrl(redirectUrl);
        });
    }
  }

}
