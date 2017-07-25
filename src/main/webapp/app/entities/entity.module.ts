import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ParentsPortalClassesModule } from './classes/classes.module';
import { ParentsPortalStudentParentsModule } from './student-parents/student-parents.module';
import { ParentsPortalInstructorModule } from './instructor/instructor.module';
import { ParentsPortalAssignmentsModule } from './assignments/assignments.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ParentsPortalClassesModule,
        ParentsPortalStudentParentsModule,
        ParentsPortalInstructorModule,
        ParentsPortalAssignmentsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParentsPortalEntityModule {}
