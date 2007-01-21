begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|TypesMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * Maps<code>java.util.Date</code> to any of the three database date/time types: TIME,  * DATE, TIMESTAMP.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|UtilDateType
extends|extends
name|AbstractType
block|{
comment|/**      * Returns "java.util.Date".      */
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Date
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Always returns true indicating no validation failures. There is no date-specific      * validations at the moment.      *       * @since 1.1      * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
specifier|public
name|boolean
name|validateProperty
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|value
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|protected
name|Object
name|convertToJdbcObject
parameter_list|(
name|Object
name|val
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|DATE
condition|)
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|TIME
condition|)
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Time
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|TIMESTAMP
condition|)
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Timestamp
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
else|else
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only DATE, TIME or TIMESTAMP can be mapped as '"
operator|+
name|getClassName
argument_list|()
operator|+
literal|"', got "
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Date
name|val
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|TIMESTAMP
case|:
name|val
operator|=
name|rs
operator|.
name|getTimestamp
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DATE
case|:
name|val
operator|=
name|rs
operator|.
name|getDate
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|TIME
case|:
name|val
operator|=
name|rs
operator|.
name|getTime
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// here the driver can "surpirse" us
comment|// check the type of returned value...
name|Object
name|object
init|=
name|rs
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|object
operator|instanceof
name|Date
operator|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Expected an instance of java.util.Date, instead got "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|", column index: "
operator|+
name|index
argument_list|)
throw|;
block|}
name|val
operator|=
operator|(
name|Date
operator|)
name|object
expr_stmt|;
break|break;
block|}
return|return
operator|(
name|rs
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
operator|new
name|Date
argument_list|(
name|val
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|cs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|val
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|TIMESTAMP
case|:
name|val
operator|=
name|cs
operator|.
name|getTimestamp
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DATE
case|:
name|val
operator|=
name|cs
operator|.
name|getDate
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|TIME
case|:
name|val
operator|=
name|cs
operator|.
name|getTime
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
default|default:
name|val
operator|=
name|cs
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// check if value was properly converted by the driver
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|val
operator|instanceof
name|java
operator|.
name|util
operator|.
name|Date
operator|)
condition|)
block|{
name|String
name|typeName
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ClassCastException
argument_list|(
literal|"Expected a java.util.Date or subclass, instead fetched '"
operator|+
name|val
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' for JDBC type "
operator|+
name|typeName
argument_list|)
throw|;
block|}
break|break;
block|}
comment|// all sql time/date classes are subclasses of java.util.Date,
comment|// so lets cast it to Date,
comment|// if it is not date, ClassCastException will be thrown,
comment|// which is what we want
return|return
operator|(
name|cs
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|(
operator|(
operator|(
name|java
operator|.
name|util
operator|.
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|setJdbcObject
argument_list|(
name|st
argument_list|,
name|convertToJdbcObject
argument_list|(
name|val
argument_list|,
name|type
argument_list|)
argument_list|,
name|pos
argument_list|,
name|type
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

