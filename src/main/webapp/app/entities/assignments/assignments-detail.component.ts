import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Assignments } from './assignments.model';
import { AssignmentsService } from './assignments.service';

@Component({
    selector: 'jhi-assignments-detail',
    templateUrl: './assignments-detail.component.html'
})
export class AssignmentsDetailComponent implements OnInit, OnDestroy {

    assignments: Assignments;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private assignmentsService: AssignmentsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAssignments();
    }

    load(id) {
        this.assignmentsService.find(id).subscribe((assignments) => {
            this.assignments = assignments;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAssignments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'assignmentsListModification',
            (response) => this.load(this.assignments.id)
        );
    }
}
