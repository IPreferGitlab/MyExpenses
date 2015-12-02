/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.totschnig.myexpenses.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.base.Joiner;

import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.MyExpenses;
import org.totschnig.myexpenses.provider.TransactionProvider;
import org.totschnig.myexpenses.provider.filter.Criteria;
import org.totschnig.myexpenses.ui.SimpleCursorAdapter;

import java.util.ArrayList;

import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ACCOUNTID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_CODE;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_CURRENCY;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_LABEL;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_PARENTID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ROWID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.TABLE_ACCOUNTS;
import static org.totschnig.myexpenses.provider.DatabaseConstants.TABLE_CURRENCIES;

public class SelectRootCategoryDialogFragment extends CommitSafeDialogFragment implements OnClickListener,
    LoaderManager.LoaderCallbacks<Cursor>
{
  protected SimpleCursorAdapter mAdapter;
  protected Cursor mCursor;

  public static final SelectRootCategoryDialogFragment newInstance() {
    return new SelectRootCategoryDialogFragment();
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.select_dialog_item, null,
        new String[] {KEY_LABEL}, new int[] {android.R.id.text1}, 0);
    getLoaderManager().initLoader(0, null, this);
    final AlertDialog dialog = new AlertDialog.Builder(getActivity())
        .setTitle(R.string.dialog_title_select_target)
        .setAdapter(mAdapter,this)
        .create();
    //dialog.getListView().setItemsCanFocus(false);
    return dialog;
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (getActivity()==null || mCursor ==null) {
      return;
    }
    //SelectFromCursorDialogListener activity = (SelectFromCursorDialogListener) getActivity();
    //Bundle args = getArguments();
    //args.putLong("result", ((AlertDialog) dialog).getListView().getItemIdAtPosition(which));
    Toast.makeText(getActivity(),""+((AlertDialog) dialog).getListView().getItemIdAtPosition(which),Toast.LENGTH_LONG).show();
    //activity.onItemSelected(args);
    dismiss();
  }
  @Override
  public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
    if (getActivity()==null) {
      return null;
    }
    String selection = KEY_PARENTID + " is null";

    CursorLoader cursorLoader = new CursorLoader(
        getActivity(),
        TransactionProvider.CATEGORIES_URI,
        null,
        selection,
        null,
        null);
    return cursorLoader;

  }
  @Override
  public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
    mCursor = data;
    mAdapter.swapCursor(data);
  }
  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    mCursor = null;
    mAdapter.swapCursor(null);
  }
}