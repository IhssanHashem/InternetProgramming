/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ParentsPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentParentsDetailComponent } from '../../../../../../main/webapp/app/entities/student-parents/student-parents-detail.component';
import { StudentParentsService } from '../../../../../../main/webapp/app/entities/student-parents/student-parents.service';
import { StudentParents } from '../../../../../../main/webapp/app/entities/student-parents/student-parents.model';

describe('Component Tests', () => {

    describe('StudentParents Management Detail Component', () => {
        let comp: StudentParentsDetailComponent;
        let fixture: ComponentFixture<StudentParentsDetailComponent>;
        let service: StudentParentsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ParentsPortalTestModule],
                declarations: [StudentParentsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudentParentsService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentParentsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentParentsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentParentsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudentParents(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studentParents).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
