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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_comment
comment|/**  * An ExtendedType that can work with any Java class, providing JDBC-to-Java mapping  * exactly per JDBC specification.  *   * @deprecated since 3.0, as explicit type mappings are created for each JDBC spec type.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultType
extends|extends
name|AbstractType
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|readMethods
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|procReadMethods
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Method
name|readObjectMethod
decl_stmt|;
specifier|private
specifier|static
name|Method
name|procReadObjectMethod
decl_stmt|;
static|static
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|rsClass
init|=
name|ResultSet
operator|.
name|class
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramTypes
init|=
operator|new
name|Class
index|[]
block|{
name|Integer
operator|.
name|TYPE
block|}
decl_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_LONG
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getLong"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BIGDECIMAL
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getBigDecimal"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BOOLEAN
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getBoolean"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BYTE
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getByte"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BYTES
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getBytes"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_SQLDATE
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getDate"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_DOUBLE
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getDouble"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_FLOAT
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getFloat"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_INTEGER
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getInt"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_SHORT
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getShort"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_STRING
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getString"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_TIME
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getTime"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_TIMESTAMP
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getTimestamp"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BLOB
argument_list|,
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getBlob"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|readObjectMethod
operator|=
name|rsClass
operator|.
name|getMethod
argument_list|(
literal|"getObject"
argument_list|,
name|paramTypes
argument_list|)
expr_stmt|;
comment|// init procedure read methods
name|Class
argument_list|<
name|?
argument_list|>
name|csClass
init|=
name|CallableStatement
operator|.
name|class
decl_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_LONG
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getLong"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BIGDECIMAL
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getBigDecimal"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BOOLEAN
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getBoolean"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BYTE
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getByte"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BYTES
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getBytes"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_SQLDATE
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getDate"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_DOUBLE
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getDouble"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_FLOAT
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getFloat"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_INTEGER
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getInt"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_SHORT
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getShort"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_STRING
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getString"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_TIME
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getTime"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_TIMESTAMP
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getTimestamp"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadMethods
operator|.
name|put
argument_list|(
name|TypesMapping
operator|.
name|JAVA_BLOB
argument_list|,
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getBlob"
argument_list|,
name|paramTypes
argument_list|)
argument_list|)
expr_stmt|;
name|procReadObjectMethod
operator|=
name|csClass
operator|.
name|getMethod
argument_list|(
literal|"getObject"
argument_list|,
name|paramTypes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error initializing read methods."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns an Iterator over the names of supported default Java classes.      */
specifier|public
specifier|static
name|Iterator
argument_list|<
name|String
argument_list|>
name|defaultTypes
parameter_list|()
block|{
return|return
name|readMethods
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|protected
name|String
name|className
decl_stmt|;
specifier|protected
name|Method
name|readMethod
decl_stmt|;
specifier|protected
name|Method
name|procReadMethod
decl_stmt|;
comment|/**      * Creates DefaultType to read objects from ResultSet using "getObject" method.      */
specifier|public
name|DefaultType
parameter_list|()
block|{
name|this
operator|.
name|className
operator|=
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|readMethod
operator|=
name|readObjectMethod
expr_stmt|;
name|this
operator|.
name|procReadMethod
operator|=
name|procReadObjectMethod
expr_stmt|;
block|}
specifier|public
name|DefaultType
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
name|this
operator|.
name|readMethod
operator|=
name|readMethods
operator|.
name|get
argument_list|(
name|className
argument_list|)
expr_stmt|;
if|if
condition|(
name|readMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported default class: "
operator|+
name|className
operator|+
literal|". If you want a non-standard class to map to JDBC type,"
operator|+
literal|" you will need to implement ExtendedType interface yourself."
argument_list|)
throw|;
block|}
name|this
operator|.
name|procReadMethod
operator|=
name|procReadMethods
operator|.
name|get
argument_list|(
name|className
argument_list|)
expr_stmt|;
if|if
condition|(
name|procReadMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported default class: "
operator|+
name|className
operator|+
literal|". If you want a non-standard class to map to JDBC type,"
operator|+
literal|" you will need to implement ExtendedType interface yourself."
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
annotation|@
name|Override
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
name|Object
name|val
init|=
name|readMethod
operator|.
name|invoke
argument_list|(
name|rs
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
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
name|val
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|st
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
name|procReadMethod
operator|.
name|invoke
argument_list|(
name|st
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|st
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
name|val
return|;
block|}
block|}
end_class

end_unit

