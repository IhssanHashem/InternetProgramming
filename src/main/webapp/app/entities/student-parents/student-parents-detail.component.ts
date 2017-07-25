import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { StudentParents } from './student-parents.model';
import { StudentParentsService } from './student-parents.service';

@Component({
    selector: 'jhi-student-parents-detail',
    templateUrl: './student-parents-detail.component.html'
})
export class StudentParentsDetailComponent implements OnInit, OnDestroy {

    studentParents: StudentParents;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentParentsService: StudentParentsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudentParents();
    }

    load(id) {
        this.studentParentsService.find(id).subscribe((studentParents) => {
            this.studentParents = studentParents;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudentParents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentParentsListModification',
            (response) => this.load(this.studentParents.id)
        );
    }
}
