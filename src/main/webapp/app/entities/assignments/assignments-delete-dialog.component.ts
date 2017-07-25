import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Assignments } from './assignments.model';
import { AssignmentsPopupService } from './assignments-popup.service';
import { AssignmentsService } from './assignments.service';

@Component({
    selector: 'jhi-assignments-delete-dialog',
    templateUrl: './assignments-delete-dialog.component.html'
})
export class AssignmentsDeleteDialogComponent {

    assignments: Assignments;

    constructor(
        private assignmentsService: AssignmentsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.assignmentsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'assignmentsListModification',
                content: 'Deleted an assignments'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-assignments-delete-popup',
    template: ''
})
export class AssignmentsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private assignmentsPopupService: AssignmentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.assignmentsPopupService
                .open(AssignmentsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
