/*
 * Copyright (c) 2012-2018, Peter Abeles. All Rights Reserved.
 *
 * This file is part of DDogleg (http://ddogleg.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ddogleg.sorting;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Peter Abeles
 */
public class TestQuickSortObj_F32 {

	Random rand = new Random(0xfeed4);

	@Test
	public void testSortingRandom() {
		SortableParameter_F32[] ret = createRandom(rand,200);

		float preTotal = sum(ret);

		QuickSortObj_F32 sorter = new QuickSortObj_F32();

		sorter.sort(ret,ret.length);

		float postTotal = sum(ret);

		// make sure it didn't modify the list, in an unexpected way
		assertEquals(preTotal,postTotal,1e-2);

		SortableParameter_F32 prev = ret[0];
		for( int i = 1; i < ret.length; i++ ) {
			if( ret[i].sortValue < prev.sortValue )
				fail("Not ascending");
			prev = ret[i];
		}
	}

	@Test
	public void testSortingRandom_indexes() {
		for( int a = 0; a < 20; a++ ) {
			SortableParameter_F32[] normal = createRandom(rand,20);
			SortableParameter_F32[] original = copy(normal);
			SortableParameter_F32[] withIndexes = copy(normal);
			int[] indexes = new int[ normal.length ];

			QuickSortObj_F32 sorter = new QuickSortObj_F32();

			sorter.sort(normal,normal.length);
			sorter.sort(withIndexes,normal.length,indexes);

			for( int i = 0; i < normal.length; i++ ) {
				// make sure the original hasn't been modified
				assertEquals(original[i].sortValue,withIndexes[i].sortValue,1e-4);
				// see if it produced the same results as the normal one
				assertEquals(normal[i].sortValue,withIndexes[indexes[i]].sortValue,1e-4);
			}
		}
	}

	public static SortableParameter_F32[] copy( SortableParameter_F32[] list ) {
		SortableParameter_F32[] ret = new SortableParameter_F32[ list.length ];
		for( int i = 0; i < list.length; i++ ) {
			ret[i] = new SortableParameter_F32();
			ret[i].sortValue = list[i].sortValue;
		}
		return ret;
	}

	public static float sum( SortableParameter_F32[] list ) {
		float total = 0;
		for( int i = 0; i < list.length; i++ ) {
			total += list[i].sortValue;
		}
		return total;
	}

	public static SortableParameter_F32[] createRandom( Random rand , final int num ) {
		SortableParameter_F32[] ret = new SortableParameter_F32[ num ];

		for( int i = 0; i < num; i++ ) {
			ret[i] = new SortableParameter_F32();
			ret[i].sortValue = (rand.nextFloat()-0.5f)*2000.0f;
		}

		return ret;
	}

}
