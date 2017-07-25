import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { StudentParents } from './student-parents.model';
import { StudentParentsPopupService } from './student-parents-popup.service';
import { StudentParentsService } from './student-parents.service';
import { User, UserService } from '../../shared';
import { Classes, ClassesService } from '../classes';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-parents-dialog',
    templateUrl: './student-parents-dialog.component.html'
})
export class StudentParentsDialogComponent implements OnInit {

    studentParents: StudentParents;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    classes: Classes[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentParentsService: StudentParentsService,
        private userService: UserService,
        private classesService: ClassesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.classesService.query()
            .subscribe((res: ResponseWrapper) => { this.classes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.studentParents.id !== undefined) {
            this.subscribeToSaveResponse(
                this.studentParentsService.update(this.studentParents));
        } else {
            this.subscribeToSaveResponse(
                this.studentParentsService.create(this.studentParents));
        }
    }

    private subscribeToSaveResponse(result: Observable<StudentParents>) {
        result.subscribe((res: StudentParents) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudentParents) {
        this.eventManager.broadcast({ name: 'studentParentsListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackClassesById(index: number, item: Classes) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-student-parents-popup',
    template: ''
})
export class StudentParentsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentParentsPopupService: StudentParentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentParentsPopupService
                    .open(StudentParentsDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentParentsPopupService
                    .open(StudentParentsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
