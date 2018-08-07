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
import {Component, Input} from '@angular/core';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { SuitesService } from '../providers/suites/suites.service';
@Component({
  selector: 'robotest-highlighter',
  styleUrls: ['highlighter.component.scss'],
  templateUrl: 'highlighter.component.html',
})
export class HighLighterComponent implements OnInit {

  @Input() href: any;
  loadedCode: string;

  constructor(private suiteService: SuitesService) {
  }

  ngOnInit() {
    this.suiteService.getSourceCapture(this.href, error => { console.log(error); })
    .then((data) => {
      this.loadedCode = data;
      console.log(data);
    }).catch((e) => {
      console.log(e);
    });

  }

}

