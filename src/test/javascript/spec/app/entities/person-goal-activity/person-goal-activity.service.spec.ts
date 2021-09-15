/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import PersonGoalActivityService from '@/entities/person-goal-activity/person-goal-activity.service';
import { PersonGoalActivity } from '@/shared/model/person-goal-activity.model';
import { Status } from '@/shared/model/enumerations/status.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('PersonGoalActivity Service', () => {
    let service: PersonGoalActivityService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PersonGoalActivityService();
      currentDate = new Date();
      elemDefault = new PersonGoalActivity('ABC', currentDate, currentDate, currentDate, Status.CREATED);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            targetDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            startDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            endDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find('ABC').then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find('ABC')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a PersonGoalActivity', async () => {
        const returnedFromService = Object.assign(
          {
            id: 'ABC',
            targetDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            startDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            endDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            targetDate: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PersonGoalActivity', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PersonGoalActivity', async () => {
        const returnedFromService = Object.assign(
          {
            targetDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            startDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            endDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            targetDate: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PersonGoalActivity', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PersonGoalActivity', async () => {
        const patchObject = Object.assign(
          {
            startDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            endDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
          },
          new PersonGoalActivity()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            targetDate: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PersonGoalActivity', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PersonGoalActivity', async () => {
        const returnedFromService = Object.assign(
          {
            targetDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            startDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            endDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            targetDate: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PersonGoalActivity', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PersonGoalActivity', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete('ABC').then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PersonGoalActivity', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete('ABC')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
