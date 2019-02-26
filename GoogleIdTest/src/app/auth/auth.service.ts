import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private serviceUrl = `${environment.apiBase}/users`;
  private googleAuthUrl = 'https://accounts.google.com/o/oauth2/v2/auth';
  private redirectUri = encodeURI(window.location.origin + '/auth/code');
  private authenticated: boolean;
  private expiresAt: number;


  constructor(private http: HttpClient) { }

  get isAuthenticated(): Promise<boolean> {
    if ((Date.now() < this.expiresAt * 1000) && this.authenticated) {
      return new Promise<boolean>(resolve => resolve(true));
    }
    return this.getUserInfo()
      .then((user) => !!user);
  }

  getUserInfo() {
    return this.http.get(`${this.serviceUrl}/me`, {
      withCredentials: true
    }).toPromise();
  }

  public login(): void {
    const authenticationUrl = `${this.googleAuthUrl}?client_id=${environment.googleClientId}&response_type=code&scope=openid%20email&redirect_uri=${this.redirectUri}`;
    const options = 'left=100,top=10,width=400,height=500';
    window.open(authenticationUrl, 'oauth', options).focus();
  }

  public authorize(code: string): Promise<any> {
    return this.http.post(`${this.serviceUrl}/authorize?code=${code}&redirect_uri=${this.redirectUri}`, null, {
      withCredentials: true
    }).toPromise()
      .then((token: IdToken) => {
        this.expiresAt = token.payload.exp;
        this.authenticated = true;
      });
  }


}

interface IdToken {
  header: {
    alg: string;
    kid: string;
    typ: string;
  };
  payload: {
    at_hash: string;
    aud: string;
    azp: string;
    email: string;
    email_verified: boolean;
    exp: number;
    iat: number;
    iss: string;
    sub: string;
  };
  signatureBytes: string;
  signedContentBytes: string;
}

