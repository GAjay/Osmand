package net.osmand.plus.parkingpoint;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.osmand.data.LatLon;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.base.MenuBottomSheetDialogFragment;

public class ParkingTypeBottomSheetDialogFragment extends MenuBottomSheetDialogFragment {

	public static final String TAG = "ParkingTypeBottomSheetDialogFragment";
	public static final String LAT_KEY = "latitude";
	public static final String LON_KEY = "longitude";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		final int themeRes = nightMode ? R.style.OsmandDarkTheme : R.style.OsmandLightTheme;
		final ParkingPositionPlugin plugin = OsmandPlugin.getEnabledPlugin(ParkingPositionPlugin.class);
		final View mainView = View.inflate(new ContextThemeWrapper(getContext(), themeRes),
				R.layout.parking_type_bottom_sheet_dialog, null);
		final LatLon latLon = new LatLon(args.getDouble(LAT_KEY), args.getDouble(LON_KEY));
		((ImageView) mainView.findViewById(R.id.by_type_icon)).setImageDrawable(getContentIcon(R.drawable.ic_action_time_start));
		((ImageView) mainView.findViewById(R.id.by_date_icon)).setImageDrawable(getContentIcon(R.drawable.ic_action_time_span));
		final MapActivity mapActivity = (MapActivity) getActivity();
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (plugin != null) {
					int id = v.getId();
					if (id == R.id.by_type_row) {
						if (plugin.isParkingEventAdded()) {
							plugin.showDeleteEventWarning(getActivity());
						}
						plugin.addOrRemoveParkingEvent(false);
						plugin.setParkingPosition(mapActivity, latLon.getLatitude(), latLon.getLongitude(), false);
						plugin.showContextMenuIfNeeded(mapActivity);
						mapActivity.refreshMap();
					} else if (id == R.id.by_date_row) {
						if (plugin.isParkingEventAdded()) {
							plugin.showDeleteEventWarning(mapActivity);
						}
						plugin.setParkingPosition(mapActivity, latLon.getLatitude(), latLon.getLongitude(), true);
						plugin.showSetTimeLimitDialog(mapActivity, new Dialog(getContext()));
						mapActivity.getMapView().refreshMap();
					}
				}
				dismiss();
			}
		};

		mainView.findViewById(R.id.by_type_row).setOnClickListener(onClickListener);
		mainView.findViewById(R.id.by_date_row).setOnClickListener(onClickListener);
		setupHeightAndBackground(mainView, R.id.scroll_view);

		return mainView;
	}
}
