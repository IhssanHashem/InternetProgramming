import { BaseEntity } from './../../shared';

export class StudentParents implements BaseEntity {
    constructor(
        public id?: number,
        public email?: string,
        public firstName?: string,
        public lastName?: string,
        public userId?: number,
        public classes?: BaseEntity[],
    ) {
    }
}
