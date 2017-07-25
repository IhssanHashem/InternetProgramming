import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Instructor } from './instructor.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class InstructorService {

    private resourceUrl = 'api/instructors';
    private resourceSearchUrl = 'api/_search/instructors';

    constructor(private http: Http) { }

    create(instructor: Instructor): Observable<Instructor> {
        const copy = this.convert(instructor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(instructor: Instructor): Observable<Instructor> {
        const copy = this.convert(instructor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Instructor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(instructor: Instructor): Instructor {
        const copy: Instructor = Object.assign({}, instructor);
        return copy;
    }
}
