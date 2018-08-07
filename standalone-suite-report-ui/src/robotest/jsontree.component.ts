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
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'robotest-jsontree',
  templateUrl: 'jsontree.component.html',
  styles: ['jsontree.component.scss']
})
export class JsonTreeComponent {
  header: any;
  breadcumKey: Array<any> = [];
  breadcumNode: Array<any> = [];
  breadcum: Array<any> = [];

  constructor(public dialogRef: MatDialogRef <JsonTreeComponent>, @Inject(MAT_DIALOG_DATA) data: any) {
    this.header = data.header;
    this.breadcum.push(data.config);
  }

  pushBreadcum( parent: any, nodeKey: any )  {
    this.breadcumKey.push(nodeKey);
    this.breadcumNode.push(parent);
    this.breadcum.push( parent[nodeKey] );
  }

  spliceBreadcum(from: number) {
    this.breadcumKey.splice(from, this.breadcumKey.length - (from));
    this.breadcumNode.splice(from, this.breadcumNode.length - (from));
    this.breadcum.splice(from + 1, this.breadcum.length - (from));
  }

  getObjectPropertiesKeys(obj): Array<string> {
    if (obj == null || obj === undefined) {
      return [];
    } else if (obj instanceof Array) {
      const tmpArray = new Array();
      for (let i = 0; i < obj.length; i++) {
        tmpArray.push(i);
      }
      return tmpArray;
    } else if (this.isBasicType(obj)) {
      const tmpArray = new Array();
      tmpArray.push(obj);
      return tmpArray;
    }
    return Object.getOwnPropertyNames(obj);
  }

  isNotArrayAndNotBasicType(obj) {
    if (!this.isArray(obj) && !this.isBasicType(obj)) {
      return true;
    }
    return false;
  }

  isArray(obj) {
    if (obj instanceof Array) {
      return true;
    }
    return false;
  }

  isBasicType(obj) {
    let resultado = false;
    if (typeof obj === 'string') {
      resultado = true;
    } else if (typeof obj === 'number') {
      resultado = true;
    } else if (typeof obj === 'boolean') {
      resultado = true;
    } else if (obj == null || obj === undefined) {
      resultado = true;
    }
    return resultado;
  }

  getBasicType(obj) {
    let resultado = 'null';
    if (typeof obj === 'string') {
      resultado = 'string';
    } else if (typeof obj === 'number') {
      resultado = 'number';
    } else if (typeof obj === 'boolean') {
      resultado = 'boolean';
    } else if (obj == null) {
      resultado = 'null';
    }
    if (obj === undefined) {
      resultado = 'undefined';
    }
    return resultado;
  }
}
