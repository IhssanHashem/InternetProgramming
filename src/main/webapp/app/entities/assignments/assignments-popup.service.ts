import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Assignments } from './assignments.model';
import { AssignmentsService } from './assignments.service';

@Injectable()
export class AssignmentsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private assignmentsService: AssignmentsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.assignmentsService.find(id).subscribe((assignments) => {
                this.assignmentsModalRef(component, assignments);
            });
        } else {
            return this.assignmentsModalRef(component, new Assignments());
        }
    }

    assignmentsModalRef(component: Component, assignments: Assignments): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.assignments = assignments;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
