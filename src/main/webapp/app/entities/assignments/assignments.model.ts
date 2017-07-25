import { BaseEntity } from './../../shared';

export class Assignments implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public assignmentNumber?: number,
        public classesId?: number,
    ) {
    }
}
