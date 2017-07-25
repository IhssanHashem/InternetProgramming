import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { StudentParents } from './student-parents.model';
import { StudentParentsService } from './student-parents.service';

@Injectable()
export class StudentParentsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private studentParentsService: StudentParentsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.studentParentsService.find(id).subscribe((studentParents) => {
                this.studentParentsModalRef(component, studentParents);
            });
        } else {
            return this.studentParentsModalRef(component, new StudentParents());
        }
    }

    studentParentsModalRef(component: Component, studentParents: StudentParents): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.studentParents = studentParents;
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
