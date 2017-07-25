import { NgModule } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    ParentsPortalSharedLibsModule,
    JhiAlertComponent,
    JhiAlertErrorComponent
} from './';

@NgModule({
    imports: [
        ParentsPortalSharedLibsModule
    ],
    declarations: [
        JhiAlertComponent,
        JhiAlertErrorComponent
    ],
    providers: [
        Title
    ],
    exports: [
        ParentsPortalSharedLibsModule,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class ParentsPortalSharedCommonModule {}
