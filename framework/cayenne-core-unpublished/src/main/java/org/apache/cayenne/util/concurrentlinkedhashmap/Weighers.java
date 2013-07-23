begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_comment
comment|/*  * Copyright 2010 Google Inc. All Rights Reserved.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|concurrentlinkedhashmap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A common set of {@link Weigher} implementations.  *   * @author ben.manes@gmail.com (Ben Manes)  * @see<a href="http://code.google.com/p/concurrentlinkedhashmap/">  *      http://code.google.com/p/concurrentlinkedhashmap/</a>  */
end_comment

begin_class
specifier|final
class|class
name|Weighers
block|{
specifier|private
name|Weighers
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
comment|/**      * A weigher where a value has a weight of<tt>1</tt>. A map bounded with this weigher      * will evict when the number of key-value pairs exceeds the capacity.      *       * @return A weigher where a value takes one unit of capacity.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|Weigher
argument_list|<
name|V
argument_list|>
name|singleton
parameter_list|()
block|{
return|return
operator|(
name|Weigher
argument_list|<
name|V
argument_list|>
operator|)
name|SingletonWeigher
operator|.
name|INSTANCE
return|;
block|}
comment|/**      * A weigher where the value is a byte array and its weight is the number of bytes. A      * map bounded with this weigher will evict when the number of bytes exceeds the      * capacity rather than the number of key-value pairs in the map. This allows for      * restricting the capacity based on the memory-consumption and is primarily for usage      * by dedicated caching servers that hold the serialized data.      *<p>      * A value with a weight of<tt>0</tt> will be rejected by the map. If a value with      * this weight can occur then the caller should eagerly evaluate the value and treat      * it as a removal operation. Alternatively, a custom weigher may be specified on the      * map to assign an empty value a positive weight.      *       * @return A weigher where each byte takes one unit of capacity.      */
specifier|public
specifier|static
name|Weigher
argument_list|<
name|byte
index|[]
argument_list|>
name|byteArray
parameter_list|()
block|{
return|return
name|ByteArrayWeigher
operator|.
name|INSTANCE
return|;
block|}
comment|/**      * A weigher where the value is a {@link Iterable} and its weight is the number of      * elements. This weigher only should be used when the alternative      * {@link #collection()} weigher cannot be, as evaluation takes O(n) time. A map      * bounded with this weigher will evict when the total number of elements exceeds the      * capacity rather than the number of key-value pairs in the map.      *<p>      * A value with a weight of<tt>0</tt> will be rejected by the map. If a value with      * this weight can occur then the caller should eagerly evaluate the value and treat      * it as a removal operation. Alternatively, a custom weigher may be specified on the      * map to assign an empty value a positive weight.      *       * @return A weigher where each element takes one unit of capacity.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Weigher
argument_list|<
name|?
super|super
name|Iterable
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterable
parameter_list|()
block|{
return|return
operator|(
name|Weigher
argument_list|<
name|Iterable
argument_list|<
name|E
argument_list|>
argument_list|>
operator|)
operator|(
name|Weigher
argument_list|<
name|?
argument_list|>
operator|)
name|IterableWeigher
operator|.
name|INSTANCE
return|;
block|}
comment|/**      * A weigher where the value is a {@link Collection} and its weight is the number of      * elements. A map bounded with this weigher will evict when the total number of      * elements exceeds the capacity rather than the number of key-value pairs in the map.      *<p>      * A value with a weight of<tt>0</tt> will be rejected by the map. If a value with      * this weight can occur then the caller should eagerly evaluate the value and treat      * it as a removal operation. Alternatively, a custom weigher may be specified on the      * map to assign an empty value a positive weight.      *       * @return A weigher where each element takes one unit of capacity.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Weigher
argument_list|<
name|?
super|super
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|>
name|collection
parameter_list|()
block|{
return|return
operator|(
name|Weigher
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|>
operator|)
operator|(
name|Weigher
argument_list|<
name|?
argument_list|>
operator|)
name|CollectionWeigher
operator|.
name|INSTANCE
return|;
block|}
comment|/**      * A weigher where the value is a {@link List} and its weight is the number of      * elements. A map bounded with this weigher will evict when the total number of      * elements exceeds the capacity rather than the number of key-value pairs in the map.      *<p>      * A value with a weight of<tt>0</tt> will be rejected by the map. If a value with      * this weight can occur then the caller should eagerly evaluate the value and treat      * it as a removal operation. Alternatively, a custom weigher may be specified on the      * map to assign an empty value a positive weight.      *       * @return A weigher where each element takes one unit of capacity.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Weigher
argument_list|<
name|?
super|super
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|list
parameter_list|()
block|{
return|return
operator|(
name|Weigher
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
operator|)
operator|(
name|Weigher
argument_list|<
name|?
argument_list|>
operator|)
name|ListWeigher
operator|.
name|INSTANCE
return|;
block|}
comment|/**      * A weigher where the value is a {@link Set} and its weight is the number of      * elements. A map bounded with this weigher will evict when the total number of      * elements exceeds the capacity rather than the number of key-value pairs in the map.      *<p>      * A value with a weight of<tt>0</tt> will be rejected by the map. If a value with      * this weight can occur then the caller should eagerly evaluate the value and treat      * it as a removal operation. Alternatively, a custom weigher may be specified on the      * map to assign an empty value a positive weight.      *       * @return A weigher where each element takes one unit of capacity.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Weigher
argument_list|<
name|?
super|super
name|Set
argument_list|<
name|E
argument_list|>
argument_list|>
name|set
parameter_list|()
block|{
return|return
operator|(
name|Weigher
argument_list|<
name|Set
argument_list|<
name|E
argument_list|>
argument_list|>
operator|)
operator|(
name|Weigher
argument_list|<
name|?
argument_list|>
operator|)
name|SetWeigher
operator|.
name|INSTANCE
return|;
block|}
comment|/**      * A weigher where the value is a {@link Map} and its weight is the number of entries.      * A map bounded with this weigher will evict when the total number of entries across      * all values exceeds the capacity rather than the number of key-value pairs in the      * map.      *<p>      * A value with a weight of<tt>0</tt> will be rejected by the map. If a value with      * this weight can occur then the caller should eagerly evaluate the value and treat      * it as a removal operation. Alternatively, a custom weigher may be specified on the      * map to assign an empty value a positive weight.      *       * @return A weigher where each entry takes one unit of capacity.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
name|Weigher
argument_list|<
name|?
super|super
name|Map
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
argument_list|>
name|map
parameter_list|()
block|{
return|return
operator|(
name|Weigher
argument_list|<
name|Map
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
argument_list|>
operator|)
operator|(
name|Weigher
argument_list|<
name|?
argument_list|>
operator|)
name|MapWeigher
operator|.
name|INSTANCE
return|;
block|}
specifier|private
enum|enum
name|SingletonWeigher
implements|implements
name|Weigher
argument_list|<
name|Object
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
literal|1
return|;
block|}
block|}
specifier|private
enum|enum
name|ByteArrayWeigher
implements|implements
name|Weigher
argument_list|<
name|byte
index|[]
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|length
return|;
block|}
block|}
specifier|private
enum|enum
name|IterableWeigher
implements|implements
name|Weigher
argument_list|<
name|Iterable
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|instanceof
name|Collection
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|values
operator|)
operator|.
name|size
argument_list|()
return|;
block|}
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|size
operator|++
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
block|}
specifier|private
enum|enum
name|CollectionWeigher
implements|implements
name|Weigher
argument_list|<
name|Collection
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|size
argument_list|()
return|;
block|}
block|}
specifier|private
enum|enum
name|ListWeigher
implements|implements
name|Weigher
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|size
argument_list|()
return|;
block|}
block|}
specifier|private
enum|enum
name|SetWeigher
implements|implements
name|Weigher
argument_list|<
name|Set
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|size
argument_list|()
return|;
block|}
block|}
specifier|private
enum|enum
name|MapWeigher
implements|implements
name|Weigher
argument_list|<
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
block|{
name|INSTANCE
block|;
specifier|public
name|int
name|weightOf
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|size
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
