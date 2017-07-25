import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { StudentParents } from './student-parents.model';
import { StudentParentsPopupService } from './student-parents-popup.service';
import { StudentParentsService } from './student-parents.service';

@Component({
    selector: 'jhi-student-parents-delete-dialog',
    templateUrl: './student-parents-delete-dialog.component.html'
})
export class StudentParentsDeleteDialogComponent {

    studentParents: StudentParents;

    constructor(
        private studentParentsService: StudentParentsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.studentParentsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'studentParentsListModification',
                content: 'Deleted an studentParents'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-student-parents-delete-popup',
    template: ''
})
export class StudentParentsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentParentsPopupService: StudentParentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentParentsPopupService
                .open(StudentParentsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
