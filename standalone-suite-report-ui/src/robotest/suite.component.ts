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
import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import {  SuitesService,
          IReport,
          ISuite,
          ICase,
          IStep,
          IValidation
          } from '../providers/suites/suites.service';
import { JsonTreeComponent } from './jsontree.component';
import { OutErrorsComponent } from './outerrors.component';

@Component({
  selector: 'robotest-suite',
  templateUrl: './suite.component.html',
  styleUrls: ['./suite.component.scss']
})
export class SuiteComponent  {

  report: IReport;
  suitesLoaded: boolean;
  suitesSize: number;
  SUITE = 'SUITE';
  CASE = 'CASE';
  STEP = 'STEP';
  VALIDATION = 'VALIDATION';
  dialogCfgRef: MatDialogRef<JsonTreeComponent>;
  dialogOERef: MatDialogRef<OutErrorsComponent>;

  public doughnutChartLabels: string[] = ['Errors', 'Infos', 'Warnings'];
  public doughnutChartType = 'doughnut';

  public lineChartLegend = false;
  public lineChartType = 'line';
  public doughnutChartOptions: any = {
    legend: {
      labels: {
        fontColor: '#000000'
      }
    }
  };
  public lineChartOptions: any = {
    responsive: true,
    scales: {
      yAxes: [{
        ticks: {
          fontColor: '#000000'
        }
      }],
      xAxes: [{
        ticks: {
          fontColor: '#000000'
        }
      }],
    },
    legend: {
      labels: {
        fontColor: '#000000'
      }
    }
  };
  public lineChartColors: Array<any> = [
    { // grey
      backgroundColor: '#c25b8d',
      borderColor: '#aa004f',
      pointBackgroundColor: '#aa004f',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: '#c25b8d'
    }
  ];


  constructor(private suiteService: SuitesService, public dialog: MatDialog) {
    this.suitesLoaded = false;
    this.getSuiteList('robotest-suites-list.json');
  }

  getSuiteList(path: string) {
    this.suiteService.getSuiteList(path, error => { console.log(error); })
    .then((data) => {
      this.report = {
        suites: new Array<ISuite>(),
        suiteSelected: null
      };
      this.suitesSize = data['suites'].length;
      for (const suiteItem of data['suites']) {
        this.getSuite(suiteItem);
      }
    })
    .catch((e) => {
      console.log(e);
    });
  }

  getSuite(path: string) {
    this.suiteService.getSuite(path, error => { console.log(error); })
      .then((data) => {
        this.report.suites.push(data);
        if (this.report.suites.length === this.suitesSize) {
          this.suitesLoaded = true;
        }
      }).catch((e) => {
        console.log(e);
      });
  }

  showConfig(selectedConfig, selectedConfigHeader): void {
    if (this.dialogCfgRef != null) {
      this.dialogCfgRef.close();
    }
    this.dialogCfgRef = this.dialog.open(JsonTreeComponent, {
      height: Math.round((window.innerHeight * 75) / 100) + 'px',
      width: Math.round((window.innerWidth * 65) / 100) + 'px',
      data: { config: selectedConfig, header: selectedConfigHeader }
    });
  }

  showOutErrors(selectedOutErrors, selectedErrorsHeader): void {
    if (this.dialogOERef != null) {
      this.dialogOERef.close();
    }
    this.dialogOERef = this.dialog.open(OutErrorsComponent, {
      height: Math.round((window.innerHeight * 75) / 100) + 'px',
      width: Math.round((window.innerWidth * 65) / 100) + 'px',
      data: { errors: selectedOutErrors, header: selectedErrorsHeader }
    });
  }

  deselectSuite(): void {
    this.deselectCase();
    this.report.suiteSelected = null;
  }

  deselectCase(): void {
    if (this.report.suiteSelected.caseSelected != null) {
      this.deselectStep();
      this.report.suiteSelected.caseSelected = null;
    }
  }

  deselectStep(): void {
    if (this.report.suiteSelected.caseSelected.stepSelected  != null) {
      this.deselectValidation();
      this.report.suiteSelected.caseSelected.stepSelected = null;
    }
  }

  deselectValidation(): void {
    this.report.suiteSelected.caseSelected.stepSelected.validationSelected = null;
  }

}
