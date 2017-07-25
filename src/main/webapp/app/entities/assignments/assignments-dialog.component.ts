import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Assignments } from './assignments.model';
import { AssignmentsPopupService } from './assignments-popup.service';
import { AssignmentsService } from './assignments.service';
import { Classes, ClassesService } from '../classes';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-assignments-dialog',
    templateUrl: './assignments-dialog.component.html'
})
export class AssignmentsDialogComponent implements OnInit {

    assignments: Assignments;
    authorities: any[];
    isSaving: boolean;

    classes: Classes[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private assignmentsService: AssignmentsService,
        private classesService: ClassesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.classesService.query()
            .subscribe((res: ResponseWrapper) => { this.classes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.assignments.id !== undefined) {
            this.subscribeToSaveResponse(
                this.assignmentsService.update(this.assignments));
        } else {
            this.subscribeToSaveResponse(
                this.assignmentsService.create(this.assignments));
        }
    }

    private subscribeToSaveResponse(result: Observable<Assignments>) {
        result.subscribe((res: Assignments) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Assignments) {
        this.eventManager.broadcast({ name: 'assignmentsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackClassesById(index: number, item: Classes) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-assignments-popup',
    template: ''
})
export class AssignmentsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private assignmentsPopupService: AssignmentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.assignmentsPopupService
                    .open(AssignmentsDialogComponent, params['id']);
            } else {
                this.modalRef = this.assignmentsPopupService
                    .open(AssignmentsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
