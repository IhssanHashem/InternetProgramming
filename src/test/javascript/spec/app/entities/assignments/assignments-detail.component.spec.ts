/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ParentsPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AssignmentsDetailComponent } from '../../../../../../main/webapp/app/entities/assignments/assignments-detail.component';
import { AssignmentsService } from '../../../../../../main/webapp/app/entities/assignments/assignments.service';
import { Assignments } from '../../../../../../main/webapp/app/entities/assignments/assignments.model';

describe('Component Tests', () => {

    describe('Assignments Management Detail Component', () => {
        let comp: AssignmentsDetailComponent;
        let fixture: ComponentFixture<AssignmentsDetailComponent>;
        let service: AssignmentsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ParentsPortalTestModule],
                declarations: [AssignmentsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AssignmentsService,
                    JhiEventManager
                ]
            }).overrideTemplate(AssignmentsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AssignmentsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AssignmentsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Assignments(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.assignments).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
