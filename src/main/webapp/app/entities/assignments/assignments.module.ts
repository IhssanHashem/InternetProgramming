import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ParentsPortalSharedModule } from '../../shared';
import {
    AssignmentsService,
    AssignmentsPopupService,
    AssignmentsComponent,
    AssignmentsDetailComponent,
    AssignmentsDialogComponent,
    AssignmentsPopupComponent,
    AssignmentsDeletePopupComponent,
    AssignmentsDeleteDialogComponent,
    assignmentsRoute,
    assignmentsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...assignmentsRoute,
    ...assignmentsPopupRoute,
];

@NgModule({
    imports: [
        ParentsPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AssignmentsComponent,
        AssignmentsDetailComponent,
        AssignmentsDialogComponent,
        AssignmentsDeleteDialogComponent,
        AssignmentsPopupComponent,
        AssignmentsDeletePopupComponent,
    ],
    entryComponents: [
        AssignmentsComponent,
        AssignmentsDialogComponent,
        AssignmentsPopupComponent,
        AssignmentsDeleteDialogComponent,
        AssignmentsDeletePopupComponent,
    ],
    providers: [
        AssignmentsService,
        AssignmentsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParentsPortalAssignmentsModule {}
