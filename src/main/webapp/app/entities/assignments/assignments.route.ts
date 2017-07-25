import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AssignmentsComponent } from './assignments.component';
import { AssignmentsDetailComponent } from './assignments-detail.component';
import { AssignmentsPopupComponent } from './assignments-dialog.component';
import { AssignmentsDeletePopupComponent } from './assignments-delete-dialog.component';

import { Principal } from '../../shared';

export const assignmentsRoute: Routes = [
    {
        path: 'assignments',
        component: AssignmentsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Assignments'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'assignments/:id',
        component: AssignmentsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Assignments'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const assignmentsPopupRoute: Routes = [
    {
        path: 'assignments-new',
        component: AssignmentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Assignments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'assignments/:id/edit',
        component: AssignmentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Assignments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'assignments/:id/delete',
        component: AssignmentsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Assignments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
