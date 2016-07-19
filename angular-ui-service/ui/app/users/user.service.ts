import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';

import { IUser } from './user';

@Injectable()
export class UserService {
    
    userId : number;
    private _userUrl = 'http://localhost:9999/api/users';
    constructor(private _http: Http) { }

    getUsers(): Observable<IUser[]> {
        return this._http.get(this._userUrl)
            .map((response: Response) => <IUser[]> response.json())
            .do(data => console.log('All: ' +  JSON.stringify(data)))
            .catch(this.handleError);
    }

    getUser2(id:number) : Observable<IUser>{
        this.userId=id;
        return this._http.get(this._userUrl + '/1')
                .map((response: Response) => <IUser> response.json())
                .do(data => console.log('All: ' +  JSON.stringify(data)))
                .catch(this.handleError);
    }
    getUser(id: number): Observable<IUser> {
        return this.getUsers()
            .map((users: IUser[]) => users.find(u => u.id === id));
    }

    private handleError(error: Response) {
        // in a real world app, we may send the server to some remote logging infrastructure
        // instead of just logging it to the console
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
    getUserID() : number {
        return this.userId;
    }
    

    
}
