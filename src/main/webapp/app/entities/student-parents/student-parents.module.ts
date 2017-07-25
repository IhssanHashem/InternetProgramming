import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ParentsPortalSharedModule } from '../../shared';
import { ParentsPortalAdminModule } from '../../admin/admin.module';
import {
    StudentParentsService,
    StudentParentsPopupService,
    StudentParentsComponent,
    StudentParentsDetailComponent,
    StudentParentsDialogComponent,
    StudentParentsPopupComponent,
    StudentParentsDeletePopupComponent,
    StudentParentsDeleteDialogComponent,
    studentParentsRoute,
    studentParentsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...studentParentsRoute,
    ...studentParentsPopupRoute,
];

@NgModule({
    imports: [
        ParentsPortalSharedModule,
        ParentsPortalAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StudentParentsComponent,
        StudentParentsDetailComponent,
        StudentParentsDialogComponent,
        StudentParentsDeleteDialogComponent,
        StudentParentsPopupComponent,
        StudentParentsDeletePopupComponent,
    ],
    entryComponents: [
        StudentParentsComponent,
        StudentParentsDialogComponent,
        StudentParentsPopupComponent,
        StudentParentsDeleteDialogComponent,
        StudentParentsDeletePopupComponent,
    ],
    providers: [
        StudentParentsService,
        StudentParentsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParentsPortalStudentParentsModule {}
