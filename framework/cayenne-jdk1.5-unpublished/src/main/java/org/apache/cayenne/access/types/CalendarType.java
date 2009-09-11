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
name|Calendar
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
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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
comment|/**  * ExtendedType that handles {@link java.util.Calendar} fields.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|CalendarType
parameter_list|<
name|T
extends|extends
name|Calendar
parameter_list|>
implements|implements
name|ExtendedType
block|{
specifier|protected
name|Class
argument_list|<
name|T
argument_list|>
name|calendarClass
decl_stmt|;
specifier|public
name|CalendarType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|calendarClass
parameter_list|)
block|{
if|if
condition|(
name|calendarClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null calendar class"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Calendar
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|calendarClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must be a java.util.Calendar or a subclass: "
operator|+
name|calendarClass
argument_list|)
throw|;
block|}
name|this
operator|.
name|calendarClass
operator|=
name|calendarClass
expr_stmt|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|calendarClass
operator|.
name|getName
argument_list|()
return|;
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
comment|// here the driver can "surprise" us
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
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|GregorianCalendar
name|calendar
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|setTime
argument_list|(
name|val
argument_list|)
expr_stmt|;
return|return
name|calendar
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
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
comment|// here the driver can "surprise" us
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
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|GregorianCalendar
name|calendar
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|setTime
argument_list|(
name|val
argument_list|)
expr_stmt|;
return|return
name|calendar
return|;
block|}
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
name|value
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
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Calendar
condition|)
block|{
name|Calendar
name|calendar
init|=
operator|(
name|Calendar
operator|)
name|value
decl_stmt|;
name|statement
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|convertToJdbcObject
argument_list|(
name|calendar
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected java.util.Calendar, got "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Object
name|convertToJdbcObject
parameter_list|(
name|Calendar
name|value
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Calendar
name|calendar
init|=
name|value
decl_stmt|;
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
name|calendar
operator|.
name|getTimeInMillis
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
name|calendar
operator|.
name|getTimeInMillis
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
name|calendar
operator|.
name|getTimeInMillis
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
comment|/**      * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
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
block|}
end_class

end_unit

