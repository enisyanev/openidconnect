import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent implements OnInit {

  constructor(private authService: AuthService,
    private router : Router,
    private currentRoute: ActivatedRoute) { }

  ngOnInit() {
    this.currentRoute.queryParams.subscribe((params) => {
      window.opener.postMessage({type: 'oauth', value: params.code}, window.opener.origin);
      window.close();
    });

  }

}
