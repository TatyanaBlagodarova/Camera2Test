package com.camera2.test.utils;

import android.annotation.TargetApi;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Range;
import android.util.Size;

import com.camera2.test.model.ResultsManager;

/**
 * Created by Tatyana Blagodarova on 5/13/17.
 */

public class KeyValueConvertor {

    public static String getValueName(CameraCharacteristics.Key key, Object value) {
        if ("android.info.supportedHardwareLevel".equals(key.getName())) {
            int intValue = (int) value;
            switch (intValue) {
                case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED:
                    return "HARDWARE_LEVEL_LIMITED";
                case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL:
                    return "HARDWARE_LEVEL_FULL";
                case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY:
                    return "HARDWARE_LEVEL_LEGACY";
                case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3:
                    return "HARDWARE_LEVEL_3";
                default:
                    return String.valueOf(value);
            }
        } else if ("android.lens.facing".equals(key.getName())) {
            int intValue = (int) value;
            switch (intValue) {
                case CameraCharacteristics.LENS_FACING_FRONT:
                    return "LENS_FACING_FRONT";
                case CameraCharacteristics.LENS_FACING_BACK:
                    return "LENS_FACING_BACK";
                case CameraCharacteristics.LENS_FACING_EXTERNAL:
                    return "LENS_FACING_EXTERNAL";
                default:
                    return String.valueOf(value);
            }
        } else if ("android.scaler.croppingType".equals(key.getName())) {
            int intValue = (int) value;
            switch (intValue) {
                case CameraCharacteristics.SCALER_CROPPING_TYPE_CENTER_ONLY:
                    return "SCALER_CROPPING_TYPE_CENTER_ONLY";
                case CameraCharacteristics.SCALER_CROPPING_TYPE_FREEFORM:
                    return "SCALER_CROPPING_TYPE_FREEFORM";
                default:
                    return String.valueOf(value);
            }
        } else if ("android.sensor.info.timestampSource".equals(key.getName())) {
            int intValue = (int) value;
            switch (intValue) {
                case CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE_UNKNOWN:
                    return "SOURCE_UNKNOWN";
                case CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE_REALTIME:
                    return "SOURCE_REALTIME";
                default:
                    return String.valueOf(value);
            }
        } else if ("android.sync.maxLatency".equals(key.getName())) {
            int intValue = (int) value;
            switch (intValue) {
                case CameraCharacteristics.SYNC_MAX_LATENCY_PER_FRAME_CONTROL:
                    return "SYNC_MAX_LATENCY_PER_FRAME_CONTROL";
                case CameraCharacteristics.SYNC_MAX_LATENCY_UNKNOWN:
                    return "SYNC_MAX_LATENCY_UNKNOWN";
                default:
                    return String.valueOf(value);
            }
        } else {
            // try using type
            if (value instanceof int[]) {
                int[] ourValue = (int[]) value;
                String result = "[";
                for (int i = 0; i < ourValue.length; i++) {
                    result += ourValue[i] + ((i == ourValue.length - 1) ? "" : ",");
                }
                result += "]";
                return result;
            } else if (value instanceof float[]) {
                float[] ourValue = (float[]) value;
                String result = "[";
                for (int i = 0; i < ourValue.length; i++) {
                    result += ourValue[i] + ((i == ourValue.length - 1) ? "" : ",");
                }
                result += "]";
                return result;
            } else if (value instanceof boolean[]) {
                boolean[] ourValue = (boolean[]) value;
                String result = "[";
                for (int i = 0; i < ourValue.length; i++) {
                    result += ourValue[i] + ((i == ourValue.length - 1) ? "" : ",");
                }
                result += "]";
                return result;
            } else if (value instanceof Range[]) {
                Range[] ourValue = (Range[]) value;
                String result = "[";
                for (int i = 0; i < ourValue.length; i++) {
                    result += ourValue[i].toString() + ((i == ourValue.length - 1) ? "" : ",");
                }
                result += "]";
                return result;
            } else if (value instanceof Size[]) {
                Size[] ourValue = (Size[]) value;
                String result = "[";
                for (int i = 0; i < ourValue.length; i++) {
                    result += ourValue[i].toString() + ((i == ourValue.length - 1) ? "" : ",");
                }
                result += "]";
                return result;
            } else if (value instanceof StreamConfigurationMap) {
                StreamConfigurationMap ourValue = (StreamConfigurationMap) value;
                StringBuilder sb = new StringBuilder();
                sb.append("<br> <ul>");
                appendOutputsString(sb, ourValue);
                appendHighSpeedVideoConfigurationsString(sb, ourValue);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    appendHighResOutputsString(sb, ourValue);
                    appendInputsString(sb, ourValue);
                    appendValidOutputFormatsForInputString(sb, ourValue);
                }
                sb.append("</ul>");
                return sb.toString();
            } else
                return String.valueOf(value);
        }
    }

    private static void appendOutputsString(StringBuilder sb, StreamConfigurationMap scalerMap) {
        int[] formats = scalerMap.getOutputFormats();
        if (formats.length == 0) {
            return;
        }
        sb.append("OutputFormats:");
        sb.append("<br>");
        sb.append(" <li> ");

        for (int format : formats) {
            Size[] sizes = scalerMap.getOutputSizes(format);
            for (Size size : sizes) {
                long minFrameDuration = scalerMap.getOutputMinFrameDuration(format, size);
                long stallDuration = scalerMap.getOutputStallDuration(format, size);
                sb.append(String.format("[%dx%d, %s(%d), min_dur:%d, stall:%d] ", size.getWidth(), size.getHeight(), formatToString(format),
                        format, minFrameDuration, stallDuration));
                sb.append("<br>");
            }
        }
        // Remove the pending ", "
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" </li> ");
    }


    public static String getCameraName(String camId) {
        int cameraPosition = ResultsManager.getInstance().getCameraIdCharacteristic().get(camId).get(CameraCharacteristics.LENS_FACING);
        switch (cameraPosition) {
            case CameraCharacteristics.LENS_FACING_FRONT:
                return "FRONT";
            case CameraCharacteristics.LENS_FACING_BACK:
                return "BACK";
            case CameraCharacteristics.LENS_FACING_EXTERNAL:
                return "EXTERNAL";
        }
        return "";
    }

    private static String formatToString(int format) {
        switch (format) {
            case ImageFormat.YV12:
                return "YV12";
            case ImageFormat.YUV_420_888:
                return "YUV_420_888";
            case ImageFormat.NV21:
                return "NV21";
            case ImageFormat.NV16:
                return "NV16";
            case PixelFormat.RGB_565:
                return "RGB_565";
            case PixelFormat.RGBA_8888:
                return "RGBA_8888";
            case PixelFormat.RGBX_8888:
                return "RGBX_8888";
            case PixelFormat.RGB_888:
                return "RGB_888";
            case ImageFormat.JPEG:
                return "JPEG";
            case ImageFormat.YUY2:
                return "YUY2";
            case ImageFormat.RAW_SENSOR:
                return "RAW_SENSOR";
            case ImageFormat.RAW_PRIVATE:
                return "RAW_PRIVATE";
            case ImageFormat.RAW10:
                return "RAW10";
            case ImageFormat.DEPTH16:
                return "DEPTH16";
            case ImageFormat.DEPTH_POINT_CLOUD:
                return "DEPTH_POINT_CLOUD";
            case ImageFormat.PRIVATE:
                return "PRIVATE";
            default:
                return "UNKNOWN";
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void appendHighResOutputsString(StringBuilder sb, StreamConfigurationMap scalerMap) {
        int[] formats = scalerMap.getOutputFormats();
        if (formats.length == 0) {
            return;
        }

        StringBuilder result = new StringBuilder();
        for (int format : formats) {
            Size[] sizes = scalerMap.getHighResolutionOutputSizes(format);
            if (sizes == null) continue;
            for (Size size : sizes) {
                long minFrameDuration = scalerMap.getOutputMinFrameDuration(format, size);
                long stallDuration = scalerMap.getOutputStallDuration(format, size);
                result.append(String.format("[w:%d, h:%d, format:%s(%d), min_duration:%d, " +
                                "stall:%d], ", size.getWidth(), size.getHeight(), formatToString(format),
                        format, minFrameDuration, stallDuration));
            }
        }
        // Remove the pending ", "
        if (result.toString().length() > 3 && result.charAt(sb.length() - 1) == ' ') {
            result.delete(sb.length() - 2, sb.length());
        }

        if (!TextUtils.isEmpty(result.toString())) {
            String before = "HighResOutputs:<br><li>";
            sb.append(before);
            sb.append(result.toString());
            sb.append(" </li> ");
            sb.append("<br>");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void appendInputsString(StringBuilder sb, StreamConfigurationMap scalerMap) {
        int[] formats = scalerMap.getInputFormats();
        if (formats.length == 0) {
            return;
        }
        sb.append("InputsString:");
        sb.append("<br>");
        sb.append(" <li> ");
        for (int format : formats) {
            Size[] sizes = scalerMap.getInputSizes(format);
            for (Size size : sizes) {
                sb.append(String.format("%dx%d, %s(%d), ", size.getWidth(),
                        size.getHeight(), formatToString(format), format));
                sb.append("<br>");
            }
        }
        // Remove the pending ", "
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" </li> ");
        sb.append("<br>");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void appendValidOutputFormatsForInputString(StringBuilder sb, StreamConfigurationMap scalerMap) {
        int[] inputFormats = scalerMap.getInputFormats();
        if (inputFormats.length == 0) {
            return;
        }
        sb.append("ValidOutputFormatsForInputString:");
        sb.append(" <li> ");
        sb.append("<br>");

        for (int inputFormat : inputFormats) {
            sb.append(String.format("%s(%d), out:", formatToString(inputFormat), inputFormat));
            int[] outputFormats = scalerMap.getValidOutputFormatsForInput(inputFormat);
            for (int i = 0; i < outputFormats.length; i++) {
                sb.append(String.format("%s(%d)", formatToString(outputFormats[i]),
                        outputFormats[i]));
                if (i < outputFormats.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("<br>");
        }
        // Remove the pending ", "
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" </li> ");
        sb.append("<br>");
    }

    private static void appendHighSpeedVideoConfigurationsString(StringBuilder sb, StreamConfigurationMap scalerMap) {
        Size[] sizes = scalerMap.getHighSpeedVideoSizes();
        if (sizes.length == 0) {
            return;
        }
        sb.append("HighSpeedVideoConfigurations:");
        sb.append("<br>");
        sb.append(" <li> ");
        for (Size size : sizes) {
            Range<Integer>[] ranges = scalerMap.getHighSpeedVideoFpsRangesFor(size);
            for (Range<Integer> range : ranges) {
                sb.append(String.format("[%dx%d, min_fps:%d, max_fps:%d], ", size.getWidth(),
                        size.getHeight(), range.getLower(), range.getUpper()));
            }
        }
        // Remove the pending ", "
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" </li> ");
        sb.append("<br>");
    }

}
