import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentParentsComponent } from './student-parents.component';
import { StudentParentsDetailComponent } from './student-parents-detail.component';
import { StudentParentsPopupComponent } from './student-parents-dialog.component';
import { StudentParentsDeletePopupComponent } from './student-parents-delete-dialog.component';

import { Principal } from '../../shared';

export const studentParentsRoute: Routes = [
    {
        path: 'student-parents',
        component: StudentParentsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StudentParents'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student-parents/:id',
        component: StudentParentsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StudentParents'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentParentsPopupRoute: Routes = [
    {
        path: 'student-parents-new',
        component: StudentParentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StudentParents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-parents/:id/edit',
        component: StudentParentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StudentParents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-parents/:id/delete',
        component: StudentParentsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StudentParents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
