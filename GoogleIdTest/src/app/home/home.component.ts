import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {  

  user: any = {
    email: '',
    oauthId: ''
  };


  constructor(private authService : AuthService,
              private router: Router) { }

  ngOnInit() {
    this.authService.getUserInfo()
      .then(user => this.user = user);

  }

  register() {
    this.router.navigateByUrl('/auth/login')
    .then((resolve) => console.log(resolve), (err) => console.log(err))
  }

}
