/*
 * Designed and developed by 2017 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.colorpickerview;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

@SuppressWarnings("WeakerAccess")
class PointMapper {
  private PointMapper() {}

  protected static Point getColorPoint(ColorPickerView colorPickerView, Point point) {
    if (colorPickerView.getColorFromBitmap(point.x, point.y) != Color.TRANSPARENT) return point;
    Point center =
        new Point(colorPickerView.getMeasuredWidth() / 2, colorPickerView.getMeasuredHeight() / 2);
    return approximatedPoint(colorPickerView, point, center);
  }

  private static Point approximatedPoint(ColorPickerView colorPickerView, Point start, Point ctr) {
    if (getDistance(start, ctr) <= 3) return ctr;
    int r = colorPickerView.getMeasuredWidth()/2;

    Point center =new Point((int)Math.floor(ctr.x+((float)(r*0.97f)/getDistance(start, ctr))*(start.x - ctr.x)), (int)Math.floor(ctr.y+((float)(r*0.97f)/getDistance(start, ctr))*(start.y - ctr.y)));
    Log.i("coords", "x:" + start.x + " d:" + getDistance(start, ctr) + " r:" +r);
    int color = colorPickerView.getColorFromBitmap(center.x, center.y);
    if (color == Color.TRANSPARENT) {
      Log.i("coords", "fail");
      return center;
    } else {
      return center;
    }

  }

  private static Point getCenterPoint(Point start, Point end) {
    return new Point((end.x + 2*start.x) / 3, (end.y + 2*start.y) / 3);
  }

  private static int getDistance(Point start, Point end) {
    return (int)
        Math.sqrt(
            Math.abs(end.x - start.x) * Math.abs(end.x - start.x)
                + Math.abs(end.y - start.y) * Math.abs(end.y - start.y));
  }
}
