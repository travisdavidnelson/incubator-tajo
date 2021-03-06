/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tajo.engine.eval;

import com.google.gson.annotations.Expose;
import org.apache.tajo.catalog.CatalogUtil;
import org.apache.tajo.catalog.Schema;
import org.apache.tajo.datum.Datum;
import org.apache.tajo.datum.NullDatum;
import org.apache.tajo.storage.Tuple;
import org.apache.tajo.util.TUtil;

import static org.apache.tajo.common.TajoDataTypes.DataType;

public class RowConstantEval extends EvalNode {
  @Expose Datum [] values;

  public RowConstantEval(Datum [] values) {
    super(EvalType.ROW_CONSTANT);
    this.values = values;
  }

  @Override
  public DataType getValueType() {
    return CatalogUtil.newSimpleDataType(values[0].type());
  }

  @Override
  public String getName() {
    return "ROW";
  }

  @Override
  public Datum eval(Schema schema, Tuple tuple) {
    return NullDatum.get();
  }

  public Datum [] getValues() {
    return values;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RowConstantEval) {
      RowConstantEval other = (RowConstantEval) obj;
      return TUtil.checkEquals(values, other.values);
    }

    return false;
  }

  public String toString() {
    return TUtil.arrayToString(values);
  }

  public void preOrder(EvalNodeVisitor visitor) {
    visitor.visit(this);
  }

  public void postOrder(EvalNodeVisitor visitor) {
    visitor.visit(this);
  }
}
