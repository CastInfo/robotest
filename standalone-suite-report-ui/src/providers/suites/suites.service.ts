/********************************************************************************
 * ROBOTEST
 * Copyright (C) 2018 CAST-INFO, S.A. www.cast-info.es
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
export interface IReport {
  suites: ISuite[];
  suiteSelected: ISuite;
}
export interface ISuite {
    name: string;
    description: string;
    order: number;
    initMillis: string;
    endMillis: string;
    durationMillis: number;
    duration: string;
    infos: number;
    warnings: number;
    errors: number;
    outCaseErrors: IValidation[];
    doughnutChartData: number[];
    lineChartLabels: Array<any>;
    lineChartData: Array<any>;
    config: any;
    cases: ICase[];
    caseSelected: ICase;
}

export interface ICase {
    name: string;
    description: string;
    order: number;
    initMillis: string;
    endMillis: string;
    durationMillis: number;
    duration: string;
    infos: number;
    warnings: number;
    errors: number;
    outStepErrors: IValidation[];
    doughnutChartData: number[];
    lineChartLabels: Array<any>;
    lineChartData: Array<any>;
    config: any;
    stepSelected: IStep;
    steps: IStep[];
}

export interface IStep {
    name: string;
    description: string;
    order: number;
    initMillis: string;
    endMillis: string;
    durationMillis: number;
    duration: string;
    status: string;
    infos: number;
    warnings: number;
    errors: number;
    config: any;
    doughnutChartData: number[];
    validationSelected: IValidation;
    validations: IValidation[];
    captures: ICapture[];
}

export interface IValidation {
    order: number;
    status: string;
    type: string;
    resource: string[];
    resource_link: string;
}

export interface ICapture {
  order: number;
  url: string;
}

@Injectable()
export class SuitesService {

  constructor(private http: HttpClient) {
  }

  getBasePath(path: string) {
    return window.location.href.substring(0, window.location.href.lastIndexOf('/') + 1) + environment.robotestSuiteBaseUri + path;
  }

  getSuiteList(path: string, errorCallback) {
    return new Promise (( resolve) => {
                                        const url = this.getBasePath(path);
                                        this.http.get(url).subscribe ( data => { resolve(data); },
                                                                       error => errorCallback(error)
                                                                     );
                                      }
                       );
  }

  getSuite(path: string, errorCallback): Promise<ISuite> {
    return new Promise<ISuite> (( resolve ) => {
      const url = this.getBasePath(path);
      return this.http.get(url)
            .subscribe ( data => {
               resolve(this.parseSuite(data));
            },  error => errorCallback(error) );
    });
  }

  parseSuite(dataSuite: any): ISuite {
    const suite: ISuite = {name: dataSuite.suite,
                description: dataSuite.description,
                order: dataSuite.order,
                initMillis: dataSuite.initMillis,
                endMillis: dataSuite.endMillis,
                durationMillis: dataSuite.endMillis - dataSuite.initMillis,
                infos : 0,
                warnings : 0,
                errors : 0,
                outCaseErrors : dataSuite.suiteOutCaseErrors,
                doughnutChartData: [],
                lineChartLabels:  [],
                lineChartData: [{data: []}],
                duration: this.timeMillisDifference(dataSuite.endMillis, dataSuite.initMillis),
                config: dataSuite.config,
                caseSelected: null,
                cases: []};
    this.parseCase(dataSuite.cases, suite.cases);
    for (let i = 0; i < suite.cases.length; i++) {
        suite.infos = suite.infos + suite.cases[i].infos;
        suite.warnings = suite.warnings + suite.cases[i].warnings;
        suite.errors = suite.errors + suite.cases[i].errors;
        suite.lineChartLabels.push(suite.cases[i].order);
        suite.lineChartData[0].data.push(suite.cases[i].durationMillis);
    }
    suite.doughnutChartData.push(suite.errors);
    suite.doughnutChartData.push(suite.infos);
    suite.doughnutChartData.push(suite.warnings);
    return suite;
  }

  parseCase(dataCase, caseArray) {
    let caseSuite: ICase;
    for (let i = 0; i < dataCase.length; i++) {
      caseSuite = {name: dataCase[i].case,
                  description: dataCase[i].description,
                  order: dataCase[i].order,
                  initMillis: dataCase[i].initMillis,
                  endMillis: dataCase[i].endMillis,
                  durationMillis: dataCase[i].endMillis - dataCase[i].initMillis,
                  doughnutChartData: [],
                  lineChartLabels:  [],
                  lineChartData: [{data: []}],
                  duration: this.timeMillisDifference(dataCase[i].endMillis, dataCase[i].initMillis),
                  infos: 0,
                  warnings: 0,
                  errors: 0,
                  outStepErrors: dataCase[i].caseOutStepErrors,
                  config: dataCase[i].config,
                  stepSelected: null,
                  steps: []};
      this.parseStep(dataCase[i].steps, caseSuite);
      for (let j = 0; j < caseSuite.steps.length; j++) {
        caseSuite.lineChartLabels.push(caseSuite.steps[j].order);
        caseSuite.lineChartData[0].data.push(caseSuite.steps[j].durationMillis);
      }
      caseSuite.doughnutChartData.push(caseSuite.errors);
      caseSuite.doughnutChartData.push(caseSuite.infos);
      caseSuite.doughnutChartData.push(caseSuite.warnings);
      caseArray.push(caseSuite);
    }
  }

  parseStep(dataStep, caseSuite) {
    let stepCase: IStep;
    for (let i = 0; i < dataStep.length; i++) {
      stepCase = {name: dataStep[i].step,
                  description: dataStep[i].description,
                  order: dataStep[i].order,
                  initMillis: dataStep[i].initMillis,
                  endMillis: dataStep[i].endMillis,
                  durationMillis: dataStep[i].endMillis - dataStep[i].initMillis,
                  duration: this.timeMillisDifference(dataStep[i].endMillis, dataStep[i].initMillis),
                  infos : 0,
                  warnings : 0,
                  errors : 0,
                  status: dataStep[i].status,
                  config: dataStep[i].config,
                  doughnutChartData: [],
                  validationSelected: null,
                  validations: [],
                  captures: []};
      if (dataStep[i].status === 'INFO' || dataStep[i].status === 'DEBUG') {
        caseSuite.infos++;
      } else if (dataStep[i].status === 'WARNING') {
        caseSuite.warnings++;
      } else if (dataStep[i].status === 'ERROR' || dataStep[i].status === 'DEFECT' || dataStep[i].status === 'CRITICAL') {
        caseSuite.errors++;
      }
      this.parseValidation(dataStep[i].validations, stepCase);
      stepCase.doughnutChartData.push(stepCase.errors);
      stepCase.doughnutChartData.push(stepCase.infos);
      stepCase.doughnutChartData.push(stepCase.warnings);
      caseSuite.steps.push(stepCase);
    }
  }

  parseValidation(dataValidation, stepCase) {
    let validationStep: IValidation;
    for (let i = 0; i < dataValidation.length; i++) {
      validationStep = {order: dataValidation[i].order,
                        status: dataValidation[i].status,
                        type: dataValidation[i].type,
                        resource: dataValidation[i].resource,
                        resource_link: this.buildUrlCapture(dataValidation[i].resource[0])
                      };
      if (validationStep.type === 'SCREENSHOT' || validationStep.type === 'HTML') {
        if (validationStep.resource !== undefined && validationStep.resource !== null && validationStep.resource.length > 0) {
          validationStep.resource_link = this.buildUrlCapture(dataValidation[i].resource[0]);
        }
        if (validationStep.type === 'SCREENSHOT') {
          stepCase.captures.push({url : validationStep.resource_link, order: i});
        }
      }
      if (validationStep.status === 'INFO' || validationStep.status === 'DEBUG') {
        stepCase.infos++;
      } else if (validationStep.status === 'WARNING') {
        stepCase.warnings++;
      } else if (validationStep.status === 'ERROR' || validationStep.status === 'DEFECT' || validationStep.status === 'CRITICAL') {
        stepCase.errors++;
      }
      stepCase.validations.push(validationStep);
    }
  }

  buildUrlCapture(path: string): string {
    if (path) {
      return this.getBasePath(path);
    }
    return null;
  }

  getSourceCapture(url: string, errorCallback) {
    return new Promise<string> (( resolve) => {
                                                this.http.get(url, { responseType: 'text' } )
                                                            .subscribe ( data => { resolve(data.toString()); },
                                                                          error => errorCallback(error) );
                                               }
                               );
  }

  timeMillisDifference(timeEnd, timeStart): string {
    return moment.duration(timeEnd - timeStart).toJSON();

  }


}
