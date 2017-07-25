import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { StudentParents } from './student-parents.model';
import { StudentParentsService } from './student-parents.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-student-parents',
    templateUrl: './student-parents.component.html'
})
export class StudentParentsComponent implements OnInit, OnDestroy {
studentParents: StudentParents[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private studentParentsService: StudentParentsService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.studentParentsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.studentParents = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.studentParentsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.studentParents = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInStudentParents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: StudentParents) {
        return item.id;
    }
    registerChangeInStudentParents() {
        this.eventSubscriber = this.eventManager.subscribe('studentParentsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
