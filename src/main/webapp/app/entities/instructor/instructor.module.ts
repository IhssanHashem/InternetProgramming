import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ParentsPortalSharedModule } from '../../shared';
import { ParentsPortalAdminModule } from '../../admin/admin.module';
import {
    InstructorService,
    InstructorPopupService,
    InstructorComponent,
    InstructorDetailComponent,
    InstructorDialogComponent,
    InstructorPopupComponent,
    InstructorDeletePopupComponent,
    InstructorDeleteDialogComponent,
    instructorRoute,
    instructorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...instructorRoute,
    ...instructorPopupRoute,
];

@NgModule({
    imports: [
        ParentsPortalSharedModule,
        ParentsPortalAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        InstructorComponent,
        InstructorDetailComponent,
        InstructorDialogComponent,
        InstructorDeleteDialogComponent,
        InstructorPopupComponent,
        InstructorDeletePopupComponent,
    ],
    entryComponents: [
        InstructorComponent,
        InstructorDialogComponent,
        InstructorPopupComponent,
        InstructorDeleteDialogComponent,
        InstructorDeletePopupComponent,
    ],
    providers: [
        InstructorService,
        InstructorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParentsPortalInstructorModule {}
