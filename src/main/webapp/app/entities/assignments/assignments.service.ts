import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Assignments } from './assignments.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AssignmentsService {

    private resourceUrl = 'api/assignments';
    private resourceSearchUrl = 'api/_search/assignments';

    constructor(private http: Http) { }

    create(assignments: Assignments): Observable<Assignments> {
        const copy = this.convert(assignments);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(assignments: Assignments): Observable<Assignments> {
        const copy = this.convert(assignments);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Assignments> {
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

    private convert(assignments: Assignments): Assignments {
        const copy: Assignments = Object.assign({}, assignments);
        return copy;
    }
}
